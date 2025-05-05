package web.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import web.dto.PhotoAnalyzeDto;

import java.util.Map;

@FeignClient(name = "ai", url = "http://localhost:3000")
public interface AiClient {

    @PostMapping(value = "/ai/photo-analyze", consumes = "application/json")
    String photoAnalyze(@RequestBody PhotoAnalyzeDto body);
}