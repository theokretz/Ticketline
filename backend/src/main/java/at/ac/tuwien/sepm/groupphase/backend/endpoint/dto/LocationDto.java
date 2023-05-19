package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;


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
