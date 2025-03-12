
package es.org.cxn.backapp.test.unit.entity;

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

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import es.org.cxn.backapp.model.persistence.team.PersistentTeamEntity;
import es.org.cxn.backapp.model.persistence.user.PersistentUserEntity;

class PersistentTeamEntityTest {

    private PersistentTeamEntity teamEntity;

    @BeforeEach
    void setUp() {
        teamEntity = new PersistentTeamEntity("ChessTeam", "Primera", "A club for chess lovers");
    }

    @Test
    void shouldCheckEquality() {
        PersistentTeamEntity sameTeam = new PersistentTeamEntity("ChessTeam", "Primera", "A club for chess lovers");
        PersistentTeamEntity differentTeam = new PersistentTeamEntity("OtherTeam", "Segunda", "Different club");

        assertThat(teamEntity).isEqualTo(sameTeam);
        assertThat(teamEntity).isNotEqualTo(differentTeam);
    }

    @Test
    void shouldGenerateSameHashCodeForEqualObjects() {
        PersistentTeamEntity sameTeam = new PersistentTeamEntity("ChessTeam", "Primera", "A club for chess lovers");
        assertThat(teamEntity).hasSameHashCodeAs(sameTeam);
    }

    @Test
    void shouldInitializeWithConstructor() {
        assertThat(teamEntity.getName()).isEqualTo("ChessTeam");
        assertThat(teamEntity.getCategory()).isEqualTo("Primera");
        assertThat(teamEntity.getDescription()).isEqualTo("A club for chess lovers");
        assertThat(teamEntity.getUsers()).isEmpty();
    }

    @Test
    void shouldSetAndGetCategory() {
        teamEntity.setCategory("Segunda");
        assertThat(teamEntity.getCategory()).isEqualTo("Segunda");
    }

    @Test
    void shouldSetAndGetDescription() {
        teamEntity.setDescription("Updated description");
        assertThat(teamEntity.getDescription()).isEqualTo("Updated description");
    }

    @Test
    void shouldSetAndGetName() {
        teamEntity.setName("NewTeam");
        assertThat(teamEntity.getName()).isEqualTo("NewTeam");
    }

    @Test
    void shouldSetAndGetUsers() {
        List<PersistentUserEntity> users = new ArrayList<>();
        users.add(new PersistentUserEntity());
        teamEntity.setUsers(users);
        assertThat(teamEntity.getUsers()).hasSize(1);
    }
}
