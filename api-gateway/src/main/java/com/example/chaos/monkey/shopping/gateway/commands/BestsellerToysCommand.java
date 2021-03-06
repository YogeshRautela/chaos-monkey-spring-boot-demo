package com.example.chaos.monkey.shopping.gateway.commands;

import com.example.chaos.monkey.shopping.domain.Product;
import com.example.chaos.monkey.shopping.gateway.domain.ProductResponse;
import com.example.chaos.monkey.shopping.gateway.domain.ResponseType;
import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;

/**
 * @author Benjamin Wilms
 */
public class BestsellerToysCommand extends HystrixCommand<ProductResponse> {

    private final RestTemplate restTemplate;
    private final String url;


    public BestsellerToysCommand(HystrixCommandGroupKey group, int timeout, RestTemplate restTemplate,
                                 String url) {
        super(group, timeout);
        this.restTemplate = restTemplate;
        this.url = url;
    }

    protected ProductResponse run() throws Exception {
        ProductResponse response = new ProductResponse();

        response.setProducts(restTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<List<Product>>() {
        }).getBody());

        response.setResponseType(ResponseType.REMOTE_SERVICE);

        return response;


    }

    @Override
    protected ProductResponse getFallback() {
        return new ProductResponse(ResponseType.FALLBACK, Collections.<Product>emptyList());
    }
}
