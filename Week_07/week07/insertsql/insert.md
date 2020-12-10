# 数据库插入数据
## 插入方式
1. insert into

    insert into是最常用的插入数据的方式，可以单条插入，也可以多条，
    还可以指定从其他表中select然后插入。

 
2. insert ignore into
 
    加上ignore，当表中有相同键的时候，忽略插入。
    因此从效果上来说，就是以旧数据为准。
 
3. replace into 

    使用replace，如果表中有冲突的键，那么先删除这样的行，然后插入。
    从效果上来说，就是以新数据为准。 
    
4. insert into on duplicate key update

    当发现冲突键的时候，有选择的更新某些列的值。
    这里有个特别的values函数，当遇到数据冲突时，可以引用values中的值来更新相关的数据：
    
    ```
    INSERT INTO table (a,b,c) 
    VALUES (1,2,3),(4,5,6) 
    ON DUPLICATE KEY UPDATE c=VALUES(a)+VALUES(b);
    ```
   
    举个例子，字段a被定义为UNIQUE，并且原数据库表table中已存在记录(2,2,9)和(3,2,1)，
    如果插入记录的a值与原有记录重复，则更新原有记录，否则插入新行： 

    ```effective
    INSERT INTO TABLE (a,b,c) VALUES 
    (1,2,3), 
    (2,5,7), 
    (3,3,6), 
    (4,8,2) 
    ON DUPLICATE KEY UPDATE b=VALUES(b); 
    ``` 
   
    以上SQL语句的执行，发现(2,5,7)中的a与原有记录(2,2,9)发生唯一值冲突，
    则执行ON DUPLICATE KEY UPDATE，
    将原有记录(2,2,9)更新成(2,5,9)，
    将(3,2,1)更新成(3,3,1)，插入新记录(1,2,3)和(4,8,2) 

    `注意`：ON DUPLICATE KEY UPDATE只是MySQL的特有语法，并不是SQL标准语法

## 不同插入方式效率比较 
1. 逐条插入
    ```  
    DROP PROCEDURE IF EXISTS proc_initData;
   
    DELIMITER $
    CREATE PROCEDURE proc_initData()
    BEGIN
    DECLARE i INT DEFAULT 1;
    WHILE i<=1000 DO
    
    INSERT INTO `items` VALUES (i, '美女最爱下午茶 奶茶泡泡更悠闲', 153, 9, 2931, 1, '<p>超级好吃、非常好吃</p><img src=\"http://122.152.205.72:88/foodie/tea-153/img1.png\"><img src=\"http://122.152.205.72:88/foodie/tea-153/img2.png\">', '2019-09-01 14:45:34', '2019-09-01 14:45:38');
    
    SET i = i+1;
    END WHILE;
    END $
    
    
    set @start=(select current_timestamp(6));    
    CALL proc_initData();
    set @end=(select current_timestamp(6));
    
    select @start;  
    select @end;   
   ```
    总共耗时31.56486s，事实上几乎每条语句花的时间是差不多的，
    基本就是30ms。
    
    这样子1000w的数据就得花87h。
    
2. 基于事务的插入  
   ```
   start transaction;
   commit;
   ```
   
   数据量 | 时间(s)
   --- | ---
    1k | 0.1458
    
    方法一每个插入语句都开启了一个事务，因此会特别慢。
    现在将所有插入语句放在一个事务。性能是有很大提升的
    
3. 单条语句一次插入多组数据
    ```
   INSERT INTO `items` VALUES
   (...),
   ...
   (...);
   ```
   数据量 | 时间(s)
   --- | ---
   1k | 0.15
   
   看上去也是对数时间，而且比方法二要稍微快一点。
   但是单次SQL语句是有缓冲区大小限制的，虽然可以修改配置让他变大，但也不能太大。
   所以在插入大批量的数据时也用不了。

4. 导入数据文件 
   >load data local infile "items-for-insert.dat" into table items;   
   
   数据量 | 时间(s)
   --- | ---
   1k | 0.13
   
   效率是最高的