
package es.org.cxn.backapp.model.persistence;

import es.org.cxn.backapp.model.PaymentSheetEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

@Entity(name = "PaymentSheet")
@Table(name = "paymentsheet")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PersistentPaymentSheetEntity implements PaymentSheetEntity {

  @Transient
  private static final long serialVersionUID = -8238192117130392730L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false, unique = true)
  private Integer id;

  @Column(name = "reason", nullable = false, unique = false)
  @NonNull
  private String reason;

  @Column(name = "place", nullable = false, unique = false)
  @NonNull
  private String place;

  @Column(name = "start_date", nullable = false, unique = false)
  @NonNull
  private LocalDate startDate;

  @Column(name = "end_date", nullable = false, unique = false)
  @NonNull
  private LocalDate endDate;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_dni", nullable = false)
  @NonNull
  private PersistentUserEntity userOwner;

  @OneToMany(
        mappedBy = "paymentSheet", cascade = CascadeType.ALL,
        orphanRemoval = true
  )
  @Builder.Default
  private Set<PersistentRegularTransportEntity> regularTransports =
        new HashSet<>();

  @OneToOne(
        mappedBy = "paymentSheet", cascade = CascadeType.ALL,
        orphanRemoval = true
  )
  private PersistentSelfVehicleEntity selfVehicle;

  @OneToOne(
        mappedBy = "paymentSheet", cascade = CascadeType.ALL,
        orphanRemoval = true
  )
  private PersistentFoodHousingEntity foodHousing;

  // Método para agregar un transporte regular
  public void addRegularTransport(PersistentRegularTransportEntity transport) {
    this.regularTransports.add(transport);
    transport.setPaymentSheet(this); // Asegúrate de establecer la relación bidireccional
  }

  // Método para eliminar un transporte regular
  public void
        deleteRegularTransport(PersistentRegularTransportEntity transport) {
    this.regularTransports.remove(transport);
    transport.setPaymentSheet(null); // Asegúrate de romper la relación bidireccional
  }
}
