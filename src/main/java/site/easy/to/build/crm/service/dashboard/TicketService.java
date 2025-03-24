package site.easy.to.build.crm.service.dashboard;

import java.util.List;

import site.easy.to.build.crm.entity.TicketStatusCount;

public interface TicketService {
    List<TicketStatusCount> getTicketStatusCounts();
}
