package com.javaschool.config.audit;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;



@MappedSuperclass
@Data
@EqualsAndHashCode(callSuper = true)
public abstract class UserDateAudit extends DateAudit {

	private static final long serialVersionUID = 1L;

	@CreatedBy
	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String createdBy;

    @LastModifiedBy
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String updatedBy;

}

