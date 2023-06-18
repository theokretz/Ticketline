package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.checkout;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class CheckoutLocation {
    private Integer locationId;
    private Integer postalCode;
    private String city;
    private String country;
    private String street;

    public Integer getLocationId() {
        return locationId;
    }

    public void setLocationId(Integer locationId) {
        this.locationId = locationId;
    }

    public Integer getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(Integer postalCode) {
        this.postalCode = postalCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public List<String> validate() {
        List<String> errors = new ArrayList<>();
        if (postalCode == null || postalCode.toString().isBlank() || city == null || city.isBlank()
            || country == null || country.isBlank() || street == null || street.isBlank()) {
            return List.of("All fields are required");
        }

        //regex for postal code
        final String postalCodeRegex = "^[0-9]{4,5}$";
        //regex for city
        final String cityRegex = "^[a-zA-ZäöüÄÖÜß]+(([',. -][a-zA-ZäöüÄÖÜß\\s])?[a-zA-ZäöüÄÖÜß]*)*$";

        //regex for street
        final String streetRegex = "^(?=.*[a-zA-ZäöüÄÖÜß])[a-zA-Z0-9äöüÄÖÜß\\s-.,#]+$";

        if (!postalCode.toString().matches(postalCodeRegex)) {
            errors.add("Invalid postal code format. Postal code must be 4 or 5 digits long");
        }
        if (!city.matches(cityRegex)) {
            errors.add("Invalid city format. City must be at least 2 characters long and contain only letters");
        }
        //County must be valid ISO country code. e.g. AT, DE, US
        if (!Set.of(Locale.getISOCountries()).contains(country)) {
            errors.add("Invalid country format. Country must be a valid ISO country code");
        }
        if (!street.matches(streetRegex)) {
            errors.add("Invalid street format.");
        }
        return errors;
    }
}
