## Redis主从模式

| 主机名        | IP地址        | 角色   |
| ------------- | ------------- | ------ |
| redis-master  | 192.168.56.11 | master |
| redis-slave01 | 192.168.56.12 | slave  |
| redis-slave02 | 192.168.56.13 | slave  |

### 分别修改三份配置文件

* redis-master

  ```javascript
  [root@redis-master ~]# grep -Ev "^$|#" /usr/local/redis/redis.conf 
  bind 192.168.56.11
  protected-mode yes
  port 6379
  daemonize yes
  pidfile /var/run/redis_6379.pid
  logfile "/var/log/redis.log"
  dir /var/redis/
  ```

* redis-slave01

  ```javascript
  [root@redis-slave01 ~]# grep -Ev "^$|#" /usr/local/redis/redis.conf
  bind 192.168.56.12
  protected-mode yes
  port 6379
  daemonize yes
  pidfile /var/run/redis_6379.pid
  logfile "/var/log/redis.log"
  dir /var/redis/
  replicaof 192.168.56.11 6379	#配置为master的从，如果master上有密码配置，还需要增加下面一项密码配置
  masterauth 123456	#配置主的密码
  ```

* redis-slave02

  ```javascript
  [root@redis-slave02 ~]# grep -Ev "^$|#" /usr/local/redis/redis.conf
  bind 192.168.56.13
  protected-mode yes
  port 6379
  daemonize yes
  pidfile /var/run/redis_6379.pid
  logfile "/var/log/redis.log"
  dir /var/redis/
  replicaof 192.168.56.11 6379	#配置为master的从
  masterauth 123456	#配置主的密码	
  ```

### 启动

```javascript
[root@redis-master ~]# systemctl start redis
[root@redis-slave01 ~]# systemctl start redis
[root@redis-slave02 ~]# systemctl start redis
[root@redis-master ~]# netstat -tulnp |grep redis
tcp        0      0 192.168.56.11:6379      0.0.0.0:*               LISTEN      1295/redis-server 1 
[root@redis-slave01 ~]# netstat -tulnp |grep redis
tcp        0      0 192.168.56.12:6379      0.0.0.0:*               LISTEN      1625/redis-server 1 
[root@redis-slave02 ~]# netstat -tulnp |grep redis
tcp        0      0 192.168.56.13:6379      0.0.0.0:*               LISTEN      1628/redis-server 1 
```

* 验证数据同步

  * master 集器写数据

  ```
  [root@redis-master ~]# redis-cli -h 192.168.56.11	
  192.168.56.11:6379> KEYS *
  (empty list or set)
  192.168.56.11:6379> set k1 123
  OK
  192.168.56.11:6379> set k2 456
  OK
  ```

  * slave01上查看是否数据同步

  ```javascript
  [root@redis-slave01 ~]# redis-cli -h 192.168.56.12	
  192.168.56.12:6379> KEYS *
  1) "k2"
  2) "k1"
  192.168.56.12:6379> get k1
  "123"
  192.168.56.12:6379> get k2
  "456"
  ```

  * slave02上查看是否数据同步

  ```javascript
  [root@redis-slave02 ~]# redis-cli -h 192.168.56.13	
  192.168.56.13:6379> KEYS *
  1) "k2"
  2) "k1"
  192.168.56.13:6379> get k1
  "123"
  192.168.56.13:6379> get k2
  "456"
  ```

## Redis哨兵模式

| 主机名称      | IP地址              | redis版本和角色说明 |
| ------------- | ------------------- | ------------------- |
| redis-master  | 192.168.56.11:6379  | redis 5.0.3（主）   |
| redis-slave01 | 192.168.56.12:6379  | redis 5.0.3（从）   |
| redis-slave02 | 192.168.56.13:6379  | redis 5.0.3（从）   |
| redis-master  | 192.168.56.11:26379 | Sentinel01          |
| redis-slave01 | 192.168.56.12:26379 | Sentinel02          |
| redis-slave02 | 192.168.56.13:26379 | Sentinel03          |

### Sentinel

Sentinel.conf配置文件主要参数：

```javascript
# 端口
port 26379
# 是否后台启动
daemonize yes
# pid文件路径
pidfile /var/run/redis-sentinel.pid
# 日志文件路径
logfile "/var/log/sentinel.log"
# 定义工作目录
dir /tmp
# 定义Redis主的别名, IP, 端口，这里的2指的是需要至少2个Sentinel认为主Redis挂了才最终会采取下一步行为
sentinel monitor mymaster 127.0.0.1 6379 2
# 如果mymaster 30秒内没有响应，则认为其主观失效
sentinel down-after-milliseconds mymaster 30000
# 如果master重新选出来后，其它slave节点能同时并行从新master同步数据的台数有多少个，显然该值越大，所有slave节点完成同步切换的整体速度越快，但如果此时正好有人在访问这些slave，可能造成读取失败，影响面会更广。最保守的设置为1，同一时间，只能有一台干这件事，这样其它slave还能继续服务，但是所有slave全部完成缓存更新同步的进程将变慢。
sentinel parallel-syncs mymaster 1
# 该参数指定一个时间段，在该时间段内没有实现故障转移成功，则会再一次发起故障转移的操作，单位毫秒
sentinel failover-timeout mymaster 180000
# 不允许使用SENTINEL SET设置notification-script和client-reconfig-script。
sentinel deny-scripts-reconfig yes
```

修改三台Sentinel的配置文件

**redsi-master**

```javascript
[root@redis-master ~]# grep -Ev "^$|#" /usr/local/redis/sentinel.conf 
port 26379
daemonize yes
pidfile "/var/run/redis-sentinel.pid"
logfile "/var/log/sentinel.log"
dir "/tmp"
sentinel monitor mymaster 192.168.56.11 6379 2
sentinel down-after-milliseconds mymaster 30000
sentinel parallel-syncs mymaster 1
sentinel failover-timeout mymaster 180000
sentinel deny-scripts-reconfig yes
```

**redis-slave01**

```javascript
[root@redis-slave01 ~]# grep -Ev "^$|#" /usr/local/redis/sentinel.conf 
port 26379
daemonize yes
pidfile "/var/run/redis-sentinel.pid"
logfile "/var/log/sentinel.log"
dir "/tmp"
sentinel monitor mymaster 192.168.56.11 6379 2
sentinel down-after-milliseconds mymaster 30000
sentinel parallel-syncs mymaster 1
sentinel failover-timeout mymaster 180000
sentinel deny-scripts-reconfig yes
```

**redis-slave02**

```javascript
[root@redis-slave02 ~]# grep -Ev "^$|#" /usr/local/redis/sentinel.conf 
port 26379
daemonize yes
pidfile "/var/run/redis-sentinel.pid"
logfile "/var/log/sentinel.log"
dir "/tmp"
sentinel monitor mymaster 192.168.56.11 6379 2
sentinel down-after-milliseconds mymaster 30000
sentinel parallel-syncs mymaster 1
sentinel failover-timeout mymaster 180000
sentinel deny-scripts-reconfig yes
```

### 启动Sentinel

启动master

```javascript
[root@redis-master ~]# redis-sentinel /usr/local/redis/sentinel.conf 
[root@redis-master ~]# ps -ef |grep redis
root      1295     1  0 14:03 ?        00:00:06 /usr/local/redis/src/redis-server 192.168.56.11:6379
root      1407     1  1 14:40 ?        00:00:00 redis-sentinel *:26379 [sentinel]
root      1412  1200  0 14:40 pts/1    00:00:00 grep --color=auto redis
```

启动slave01

```javascript
[root@redis-slave01 ~]# redis-sentinel /usr/local/redis/sentinel.conf 
[root@redis-slave01 ~]# ps -ef |grep redis
root      1625     1  0 14:04 ?        00:00:06 /usr/local/redis/src/redis-server 192.168.56.12:6379
root      1715     1  1 14:41 ?        00:00:00 redis-sentinel *:26379 [sentinel]
root      1720  1574  0 14:41 pts/0    00:00:00 grep --color=auto redis
```

启动slave02

```javascript
[root@redis-slave02 ~]# redis-sentinel /usr/local/redis/sentinel.conf 
[root@redis-slave02 ~]# ps -ef |grep redis
root      1628     1  0 14:07 ?        00:00:06 /usr/local/redis/src/redis-server 192.168.56.13:6379
root      1709     1  0 14:42 ?        00:00:00 redis-sentinel *:26379 [sentinel]
root      1714  1575  0 14:42 pts/0    00:00:00 grep --color=auto redis
```

### Sentinel操作

哨兵模式查看, 并输出被监控的主节点的状态信息

```javascript
[root@redis-master ~]# redis-cli -p 26379	
127.0.0.1:26379> sentinel master mymaster	
 1) "name"
 2) "mymaster"
 3) "ip"
 4) "192.168.56.11"
 5) "port"
 6) "6379"
 7) "runid"
 8) "bae06cc3bc6dcbff7c2de1510df7faf1a6eb6941"
 9) "flags"
10) "master"
......
```

查看mymaster的从信息，可以看到有2个从节点

```javascript
127.0.0.1:26379> sentinel slaves mymaster	
1)  1) "name"
    2) "192.168.56.12:6379"
    3) "ip"
    4) "192.168.56.12"
    5) "port"
    6) "6379"
    7) "runid"
    8) "c86027e7bdd217cb584b1bd7a6fea4ba79cf6364"
    9) "flags"
   10) "slave"
......
2)  1) "name"
    2) "192.168.56.13:6379"
    3) "ip"
    4) "192.168.56.13"
    5) "port"
    6) "6379"
    7) "runid"
    8) "61597fdb615ecf8bd7fc18e143112401ed6156ec"
    9) "flags"
   10) "slave"
......
```

查看其它sentinel信息

```javascript
127.0.0.1:26379> sentinel sentinels mymaster	
1)  1) "name"
    2) "ba12e2a4023d2e9bcad282395ba6b14030920070"
    3) "ip"
    4) "192.168.56.12"
    5) "port"
    6) "26379"
    7) "runid"
    8) "ba12e2a4023d2e9bcad282395ba6b14030920070"
    9) "flags"
   10) "sentinel"
......
2)  1) "name"
    2) "14fca3f851e9e1bd3a4a0dc8a9e34bb237648455"
    3) "ip"
    4) "192.168.56.13"
    5) "port"
    6) "26379"
    7) "runid"
    8) "14fca3f851e9e1bd3a4a0dc8a9e34bb237648455"
    9) "flags"
   10) "sentinel"
```

### 哨兵模式下的主从测试

模拟停止master上的Redis，查看Redis的主从变化

停止master 

```javascript
[root@redis-master ~]# systemctl stop redis		
```

查看哨兵日志

```javascript
[root@redis-slave01 ~]# tail -n 20 /var/log/sentinel.log 	
......
1747:X 02 Jan 2021 14:59:01.747 # +monitor master mymaster 192.168.56.11 6379 quorum 2
1747:X 02 Jan 2021 14:59:44.829 # +sdown sentinel 14fca3f851e9e1bd3a4a0dc8a9e34bb237648455 192.168.56.13 26379 @ mymaster 192.168.56.11 6379
1747:X 02 Jan 2021 14:59:46.950 # -sdown sentinel 14fca3f851e9e1bd3a4a0dc8a9e34bb237648455 192.168.56.13 26379 @ mymaster 192.168.56.11 6379
1747:X 02 Jan 2021 15:00:44.391 # +sdown master mymaster 192.168.56.11 6379
1747:X 02 Jan 2021 15:00:44.525 # +new-epoch 1
1747:X 02 Jan 2021 15:00:44.527 # +vote-for-leader 14fca3f851e9e1bd3a4a0dc8a9e34bb237648455 1
1747:X 02 Jan 2021 15:00:45.023 # +config-update-from sentinel 14fca3f851e9e1bd3a4a0dc8a9e34bb237648455 192.168.56.13 26379 @ mymaster 192.168.56.11 6379
1747:X 02 Jan 2021 15:00:45.023 # +switch-master mymaster 192.168.56.11 6379 192.168.56.13 6379
1747:X 02 Jan 2021 15:00:45.024 * +slave slave 192.168.56.12:6379 192.168.56.12 6379 @ mymaster 192.168.56.13 6379
1747:X 02 Jan 2021 15:00:45.024 * +slave slave 192.168.56.11:6379 192.168.56.11 6379 @ mymaster 192.168.56.13 6379
1747:X 02 Jan 2021 15:01:15.050 # +sdown slave 192.168.56.11:6379 192.168.56.11 6379 @ mymaster 192.168.56.13 6379
```

从上面的日志可以看到master已经sdown，并切换为192.168.56.13为master节点，下面查看slave01上的配置，会自动的更改replicaof配置项

```javascript
[root@redis-slave01 ~]# grep "replicaof" /usr/local/redis/redis.conf |grep -vE "#"
replicaof 192.168.56.13 6379

[root@redis-master ~]# redis-cli -p 26379	#哨兵模式下查看主从信息，也是可以看到主从的变化
127.0.0.1:26379> sentinel master mymaster
 1) "name"
 2) "mymaster"
 3) "ip"
 4) "192.168.56.13"
 5) "port"
 6) "6379"
 7) "runid"
 8) "61597fdb615ecf8bd7fc18e143112401ed6156ec"
 9) "flags"
10) "master"
......
127.0.0.1:26379> sentinel slaves mymaster
1)  1) "name"
    2) "192.168.56.12:6379"
    3) "ip"
    4) "192.168.56.12"
    5) "port"
    6) "6379"
    7) "runid"
    8) "c86027e7bdd217cb584b1bd7a6fea4ba79cf6364"
    9) "flags"
   10) "slave"
......
2)  1) "name"
    2) "192.168.56.11:6379"
    3) "ip"
    4) "192.168.56.11"
    5) "port"
    6) "6379"
    7) "runid"
    8) ""
    9) "flags"
   10) "s_down,slave,disconnected"	#提示该节点为从，并且状态为s_down，无法链接的状态
   ......
```

