package site.easy.to.build.crm.service.dashboard;


import site.easy.to.build.crm.entity.ClientStat;
import site.easy.to.build.crm.repository.ClientStatRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClientStatService {

    private final ClientStatRepository clientStatRepository;

    public ClientStatService(ClientStatRepository clientStatRepository) {
        this.clientStatRepository = clientStatRepository;
    }

    public List<ClientStat> getAllClientStats() {
        return clientStatRepository.findAll();
    }
}
