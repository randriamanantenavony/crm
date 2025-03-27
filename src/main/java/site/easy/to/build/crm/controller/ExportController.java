package site.easy.to.build.crm.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import jakarta.servlet.http.HttpServletResponse;
import site.easy.to.build.crm.entity.Customer;
import site.easy.to.build.crm.entity.Lead;
import site.easy.to.build.crm.entity.Ticket;
import site.easy.to.build.crm.repository.CustomerRepository;
import site.easy.to.build.crm.repository.LeadRepository;
import site.easy.to.build.crm.service.customer.CustomerServiceImpl;
import site.easy.to.build.crm.service.lead.LeadServiceImpl;
import site.easy.to.build.crm.service.ticket.TicketServiceImpl;

import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;


@Controller
public class ExportController {
    
    @Autowired
    private final LeadServiceImpl LeadServiceImpl;

    @Autowired
    private final TicketServiceImpl TicketServiceImpl;

    @Autowired
    private final CustomerServiceImpl CustomerServiceImpl;

    public ExportController(LeadServiceImpl l, TicketServiceImpl t, CustomerServiceImpl c){
        this.LeadServiceImpl = l;
        this.TicketServiceImpl = t;
        this.CustomerServiceImpl = c;
    }

    @GetMapping("/api/admin/export/{id}")
    public void exportToCsv(HttpServletResponse response, @PathVariable("id") int id) throws IOException {
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        String currentDateTime = dateFormatter.format(new Date());

        String fileName = "users_" + currentDateTime + ".csv";

        String filePath = "D:/exports/" + fileName;

        Customer c = CustomerServiceImpl.findByCustomerId(id);
        List<Ticket> allTicket = TicketServiceImpl.findCustomerTickets(id);
        List<Lead> allLead = LeadServiceImpl.getCustomerLeads(id);

        c.setEmail("copy_"+c.getEmail());
        FileWriter writer = new FileWriter(filePath);
        ICsvBeanWriter csvWriter = new CsvBeanWriter(writer, CsvPreference.STANDARD_PREFERENCE);

        String[] csvHeader = {"name","email","position","phone","address"};
        String[] nameMapping = {"name", "email","position","phone","address"};
        String[] csvHeader2 = {"subject","description","status","priority"};
        String[] ticketMapping = {"subject", "description","status","priority"};
        String[] csvHeader3 = {"name","status","phone"};
        String[] LeadMapping = {"name", "status","phone"};

        csvWriter.writeHeader(csvHeader);
        csvWriter.write(c, nameMapping);

        csvWriter.writeHeader(csvHeader2);

        for (Ticket t: allTicket) {
            csvWriter.write(t, ticketMapping);
        }

        csvWriter.writeHeader(csvHeader3);

        for (Lead lead : allLead) {
            csvWriter.write(lead,LeadMapping);
        }

        csvWriter.close();
        writer.close();
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
        File file = new File(filePath);
        InputStream inputStream = new FileInputStream(file);
        OutputStream outputStream = response.getOutputStream();
        byte[] buffer = new byte[4096];
        int bytesRead;

        while ((bytesRead = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }

        inputStream.close();
        outputStream.flush();
        outputStream.close();
    }


}
