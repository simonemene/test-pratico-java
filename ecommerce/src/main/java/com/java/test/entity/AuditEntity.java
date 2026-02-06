package com.java.test.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
public class AuditEntity {

	@CreatedBy
	@Column(name = "UTENTE_INS")
	private String utenteInserimento;

	@CreatedDate
	@Column(name = "TIMESTAMP_INS")
	private String timestampInserimento;

	@LastModifiedBy
	@Column(name = "UTENTE_MODIFICA")
	private String utenteModifica;

	@LastModifiedDate
	@Column(name = "TIMESTAMP_MODIFICA")
	private String timestampModifica;

	@Column(name = "FLG_ANNULLO")
	private String flgAnnullo;

    @PrePersist
	public void init()
	{
		if(flgAnnullo == null)
		{
			this.flgAnnullo = "N";
		}
	}
}
