package com.example.productservice.ShippingCompany;

import com.example.productservice.Product.Product;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

// passay library
import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;
import org.passay.PasswordGenerator;

@Entity
@Table(name = "selling_company")
public class SellingCompany {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    // unique
    @Column(unique = true)
    private String name;
    private String password;        //auto generated

    private Boolean is_logged_in;

    @OneToMany(mappedBy = "sellingCompany", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Product> products;

    public SellingCompany() {
        products = new ArrayList<>();
        this.password = this.generateSecurePassword();
        this.is_logged_in = false;
    }
    public SellingCompany(String companyName) {
        this.name = companyName;
        this.password = this.generateSecurePassword();
        this.is_logged_in = false;
        products = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void addProduct(Product product) {
        products.add(product);
        product.setSellingCompany(this);
    }

    public void removeProduct(Product product) {
        products.remove(product);
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public Boolean getIs_logged_in() {
        return is_logged_in;
    }

    public void setIs_logged_in(Boolean is_logged_in) {
        this.is_logged_in = is_logged_in;
    }

    // auto generate password
    private String generateSecurePassword() {
        // Implementation to generate a random password using "Passay"

        // create character rule for lower case
        CharacterRule LCR = new CharacterRule(EnglishCharacterData.LowerCase);
        // set number of lower case characters
        LCR.setNumberOfCharacters(2);

        // create character rule for upper case
        CharacterRule UCR = new CharacterRule(EnglishCharacterData.UpperCase);
        // set number of upper case characters
        UCR.setNumberOfCharacters(2);

        // create character rule for digit
        CharacterRule DR = new CharacterRule(EnglishCharacterData.Digit);
        // set number of digits
        DR.setNumberOfCharacters(2);

        // create character rule for lower case
        CharacterRule SR = new CharacterRule(EnglishCharacterData.Special);
        // set number of special characters
        SR.setNumberOfCharacters(2);

        // create instance of the PasswordGenerator class
        PasswordGenerator passGen = new PasswordGenerator();

        // call generatePassword() method of PasswordGenerator class to get Passay generated password
        String password = passGen.generatePassword(8, SR, LCR, UCR, DR);

        // return Passay generated password to the main() method
        return password;
    }
    @Override
    public String toString() {
        return "SellingCompany{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", products=" + products +
                '}';
    }
}