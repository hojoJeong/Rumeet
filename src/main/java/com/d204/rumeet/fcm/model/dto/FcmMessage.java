package com.d204.rumeet.fcm.model.dto;


import lombok.*;

import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class FcmMessage {
    private boolean validate_only;
    private Message message;


	@AllArgsConstructor
	@NoArgsConstructor
	@Builder
	@Data
	public static class Message {
        private Notification notification;
        private String token;

		@Getter
		@Setter
		private Map<String, String> data;
    }


	@NoArgsConstructor
	@AllArgsConstructor
	@Builder
	@Data
    public static class Notification {
        private String title;
        private String body;
        private String image;
        
		public Notification(String title, String body) {
			super();
			this.title = title;
			this.body = body;
		}
    }
}
