package com.qnkj.core.base.modules.lcdp.pagedesign.entitys;

import com.github.restapi.models.Profile;
import lombok.Getter;
import lombok.Setter;

/**
* @author Auto Generator
* @date 2021-06-25
*/
@Getter
@Setter
public class User {
	private String profileid = "";
	private String id = "";
	private String fullname = "";
	private String mobile = "";
	private String email = "";
	private String givenname = "";
	private String link = "";
	private String gender = "";
	private String identitycard = "";
	private String city = "";
	private String province = "";
	private String birthdate = "";
	public User() {}
	public User(Profile profile) {
		profileid = profile.id;
		id = profile.id;
		fullname = profile.fullname;
		mobile = profile.mobile;
		email = profile.email;
		givenname = profile.givenname;
		link = profile.link;
		gender = profile.gender;
		identitycard = profile.identitycard;
		city = profile.city;
		province = profile.province;
		birthdate = profile.birthdate;
	}
}
