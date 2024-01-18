package org.zerock.b01.dto;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@ToString


public class MemberRegisterDTO {

    private String mid;
    private String mpw;
    private String mname;
    private Integer mage;
    private String maddress;
    private String msex;
    private String email;



    private boolean del;
    private boolean social;
}
