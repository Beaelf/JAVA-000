package com.megetood.geek.week03.gateway.router;

import java.util.List;

public interface HttpEndpointRouter {
    
    String route(List<String> endpoints);
    
}
