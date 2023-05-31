package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;


import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class LocationDto {

    private Integer postalCode;
    private String city;
    private String country;
    private String street;

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
        final String cityRegex = "^[a-zA-ZäöüÄÖÜ]+(([',. -][a-zA-ZäöüÄÖÜ\\s])?[a-zA-ZäöüÄÖÜ]*)*$";

        //regex for street
        final String streetRegex = "^[a-zA-Z0-9äöüÄÖÜ\\s-.,#]+$";

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

    public static final class LocationDtoBuilder {
        private Integer postalCode;
        private String city;
        private String country;
        private String street;

        private LocationDtoBuilder() {
        }

        public static LocationDtoBuilder aLocationDto() {
            return new LocationDtoBuilder();
        }

        public LocationDtoBuilder withPostalCode(Integer postalCode) {
            this.postalCode = postalCode;
            return this;
        }

        public LocationDtoBuilder withCity(String city) {
            this.city = city;
            return this;
        }

        public LocationDtoBuilder withCountry(String country) {
            this.country = country;
            return this;
        }

        public LocationDtoBuilder withStreet(String street) {
            this.street = street;
            return this;
        }

        public LocationDto build() {
            LocationDto locationDto = new LocationDto();
            locationDto.setPostalCode(postalCode);
            locationDto.setCity(city);
            locationDto.setCountry(country);
            locationDto.setStreet(street);
            return locationDto;
        }
    }
}
