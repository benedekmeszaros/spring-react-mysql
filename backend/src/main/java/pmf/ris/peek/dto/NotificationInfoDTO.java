package pmf.ris.peek.dto;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class NotificationInfoDTO {
	private int id;
	private String content;
	private String action;
	private String userImage;
	private Instant sentDate;
	private boolean seen;
}
