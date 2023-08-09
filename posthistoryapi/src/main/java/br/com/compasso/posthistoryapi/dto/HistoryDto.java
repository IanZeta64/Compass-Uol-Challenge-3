package br.com.compasso.posthistoryapi.dto;

import br.com.compasso.posthistoryapi.enums.Status;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import java.time.Instant;

public record HistoryDto( Long id, Instant date, Status status) {
}
