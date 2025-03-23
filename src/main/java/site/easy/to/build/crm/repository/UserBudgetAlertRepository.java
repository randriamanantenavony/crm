package site.easy.to.build.crm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import site.easy.to.build.crm.entity.UserBudgetAlert;

@Repository
public interface UserBudgetAlertRepository extends JpaRepository<UserBudgetAlert, Integer> {

    @Query("SELECT u FROM UserBudgetAlert u WHERE u.userId = :userId")
    UserBudgetAlert findByUserId(@Param("userId") Integer userId);
}

