package testTask.mapper;

import org.mapstruct.Mapper;
import testTask.domain.Card;
import testTask.domain.dto.CardDto;

@Mapper(componentModel = "spring")
public interface CardMapper {
    default CardDto toDto(Card card) {
        String masked;
        masked = "************" + card.getLast4Numbers();

        return new CardDto(card.getId(), masked);
    }
}
