package site.easy.to.build.crm.service.depense;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import site.easy.to.build.crm.entity.UserBudgetAlert;
import site.easy.to.build.crm.repository.UserBudgetAlertRepository;

@Service
public class UserBudgetAlertService {

    private final UserBudgetAlertRepository userBudgetAlertRepository;

    @Autowired
    public UserBudgetAlertService(UserBudgetAlertRepository userBudgetAlertRepository) {
        this.userBudgetAlertRepository = userBudgetAlertRepository;
    }

    public UserBudgetAlert getUserBudgetAlert(Integer userId) {
        return userBudgetAlertRepository.findByUserId(userId);
    }
}

