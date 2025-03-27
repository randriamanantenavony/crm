package site.easy.to.build.crm.service.budget;

import java.util.List;

import org.springframework.stereotype.Service;

import site.easy.to.build.crm.entity.Budget;
import site.easy.to.build.crm.entity.BudgetInfo;
import site.easy.to.build.crm.repository.BudgetInfoRepository;
import site.easy.to.build.crm.repository.BudgetRepository;

@Service
public class BudgetService {
    
    private final BudgetRepository budgetRepository;

    private final BudgetInfoRepository budgetInfo;

    public BudgetService(BudgetRepository budgetRepository,BudgetInfoRepository budgetInfoRepository) {
        this.budgetRepository = budgetRepository;
        this.budgetInfo = budgetInfoRepository;
    }

    public Budget save(Budget budget) {
        return budgetRepository.save(budget);
    }

    public List<Budget> findAll() {
        return budgetRepository.findAll();
    }
    public List<BudgetInfo> getBudgetInfo()
    {
        return budgetInfo.findAll();
    }
}
