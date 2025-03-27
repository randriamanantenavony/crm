package site.easy.to.build.crm.entity;

public class CustomerBudget {
    private String email;
    private double budget;

    // Constructeur
    public CustomerBudget(String email, double budget) {
        this.email = email;
        this.budget = budget;
    }

    // Getters et Setters
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public double getBudget() {
        return budget;
    }

    public void setBudget(double budget) {
        this.budget = budget;
    }

    @Override
    public String toString() {
        return "CustomerBudget{email='" + email + "', budget=" + budget + "}";
    }
}

