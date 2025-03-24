package site.easy.to.build.crm.controller;

import site.easy.to.build.crm.entity.ClientStat;
import site.easy.to.build.crm.service.dashboard.ClientStatService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ClientStatController {

    private final ClientStatService clientStatService;

    public ClientStatController(ClientStatService clientStatService) {
        this.clientStatService = clientStatService;
    }

    @GetMapping("/api/client-stats")
    public List<ClientStat> getAllStats() {
        return clientStatService.getAllClientStats();
    }
}
