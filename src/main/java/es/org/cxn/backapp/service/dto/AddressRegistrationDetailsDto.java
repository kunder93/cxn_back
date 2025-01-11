
package es.org.cxn.backapp.service.dto;

/*-
 * #%L
 * back-app
 * %%
 * Copyright (C) 2022 - 2025 Circulo Xadrez Naron
 * %%
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 * #L%
 */

/**
 * Data Transfer Object (DTO) representing the details of a user's address.
 * <p>
 * This DTO includes fields for the apartment number, building, city, postal
 * code, street, country numeric code, and country subdivision name.
 * It is designed to facilitate the transfer of address-related data within
 * the application.
 * </p>
 *
 * <p>
 * This class is implemented as a record, providing an immutable and concise
 * way to represent data. Each field in the record corresponds to a property
 *  of the user's address.
 * </p>
 *
 * @param apartmentNumber the apartment number associated with the address
 * @param building the building name or number associated with the address
 * @param city the city where the address is located
 * @param postalCode the postal code for the address
 * @param street the street name or number of the address
 * @param countryNumericCode the numeric code representing the country of
 * the address
 * @param countrySubdivisionName the name of the country's
 * subdivision (e.g., state or province).
 *
 * @author Santi
 */

public record AddressRegistrationDetailsDto(
      String apartmentNumber, String building, String city, String postalCode,
      String street, Integer countryNumericCode, String countrySubdivisionName
) {
}
