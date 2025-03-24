package site.easy.to.build.crm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.easy.to.build.crm.entity.TotalCount;
import site.easy.to.build.crm.service.TotalCountService;

@RestController
@RequestMapping("/api/total-count")
@CrossOrigin(origins = "*")
public class TotalCountController {

    @Autowired
    private TotalCountService totalCountService;

    @GetMapping("/getAll")
    public TotalCount getTotalCount() {
        return totalCountService.getTotalCount();
    }
}
