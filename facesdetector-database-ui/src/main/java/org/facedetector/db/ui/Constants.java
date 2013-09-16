package org.facedetector.db.ui;

public interface Constants {

	enum Gender {
		MALE(1),
		FEMALE(2);
		
		private final int gender;

		private Gender(int gender) {
			this.gender = gender;
		}
		
		public int gender() {
			return gender;
		}
		
		public static Gender[] ALL_GENDERS = new Gender[] {
			MALE, FEMALE
		};
		
	};
	
	enum Ages {
		;
		private final String ages;

		private Ages(String ages) {
			this.ages = ages;
		}
		
		public String ages() {
			return ages;
		}
		
	}
	
}
