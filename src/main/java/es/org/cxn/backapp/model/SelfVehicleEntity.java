/**
 * The MIT License (MIT)
 * <p>
 * Copyright (c) 2021 the original author or authors.
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package es.org.cxn.backapp.model;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * A Invoice entity interface.
 *
 * @author Santiago Paz Perez.
 */
public interface SelfVehicleEntity extends Serializable {

  /**
   * @return The identifier.
   */
  Integer getIdentifier();

  /**
   * @return The places.
   */
  String getPlaces();

  /**
   * @return The distance.
   */
  BigDecimal getDistance();

  /**
   * @return Price per kilometer.
   */
  BigDecimal getKmPrice();

  /**
   * @param id The identifier.
   */
  void setIdentifier(Integer id);

  /**
   * @param places The places.
   */
  void setPlaces(String places);

  /**
   * @param distance The distance.
   */
  void setDistance(BigDecimal distance);

  /**
   * @param kmPrice The price per kilometer.
   */
  void setKmPrice(BigDecimal kmPrice);

}
