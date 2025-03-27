package site.easy.to.build.crm.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import site.easy.to.build.crm.entity.BudgetInfo;

public interface BudgetInfoRepository extends JpaRepository<BudgetInfo, Long> {
    // Méthode pour récupérer toutes les informations de budget
    List<BudgetInfo> findAll();
}
