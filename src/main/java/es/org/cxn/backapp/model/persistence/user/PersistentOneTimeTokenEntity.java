package es.org.cxn.backapp.model.persistence.user;

/*-
 * #%L
 * CXN-back-app
 * %%
 * Copyright (C) 2022 - 2025 Círculo Xadrez Narón
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

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * Entity representing a One-Time Token (OTT) used for authentication purposes,
 * such as password recovery.
 * <p>
 * This entity is stored in the database and linked to a user, containing
 * information about its validity and usage.
 * </p>
 *
 */
@Entity
@Table(name = "one_time_token")
@Getter
@Setter
@AllArgsConstructor
@Builder
public class PersistentOneTimeTokenEntity {

    /**
     * The token max allowed length.
     */
    private static final int TOKEN_LENGTH = 255;

    /**
     * The unique value of the One-Time Token (OTT).
     */
    @Id
    @Column(name = "token_value", length = TOKEN_LENGTH, nullable = false, unique = true)
    private String tokenValue;

    /**
     * The user associated with this One-Time Token.
     */
    @ManyToOne
    @JoinColumn(name = "user_dni", nullable = false)
    private PersistentUserEntity user;

    /**
     * The timestamp when the token was created. This value is set at creation time
     * and cannot be updated.
     */
    @Builder.Default
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt = Instant.now();

    /**
     * The timestamp when the token expires. After this time, the token is no longer
     * valid.
     */
    @Column(name = "expired_at", nullable = false)
    private Instant expiredAt;

    /**
     * Indicates whether the token has already been used. Once used, it should not
     * be valid for further authentication.
     */
    @Builder.Default
    @Column(name = "is_used", nullable = false)
    private boolean used = false;

    /**
     * Default constructor required by JPA.
     */
    public PersistentOneTimeTokenEntity() {
        this.createdAt = Instant.now();
        this.used = false;
    }
}
