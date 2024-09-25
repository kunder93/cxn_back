package es.org.cxn.backapp.service.dto;

public record CreateLichessProfileDto(String userEmail, String id, String username, GameStatistics blitz,
        GameStatistics bullet, GameStatistics classical, GameStatistics rapid, GameStatistics puzzle,
        Integer fideRating) {
    public record GameStatistics(Integer games, Integer rating, Integer rd, Integer prog, Boolean prov) {
    }
}
