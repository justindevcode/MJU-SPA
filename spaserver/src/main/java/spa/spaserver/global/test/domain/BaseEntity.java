package spa.spaserver.global.test.domain;

import java.io.Serializable;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@EntityListeners(AuditingEntityListener.class)
@Getter
@MappedSuperclass
public class BaseEntity implements Serializable {

	@CreatedDate
	@Column(updatable = false, name = "created_at")
	private LocalDateTime createdAt;

	@LastModifiedDate
	@Column(name = "update_at")
	private LocalDateTime updateAt;

	@Column(name = "delete_at")
	private LocalDateTime deleteAt;

	@PrePersist
	public void prePersist() {
		LocalDateTime now = LocalDateTime.now();
		createdAt = now;
		updateAt = now;
	}

	@PreUpdate
	public void preUpdate() {
		updateAt = LocalDateTime.now();
	}


	public void delete() {
		deleteAt = LocalDateTime.now();
	}


	public void undelete() {
		deleteAt = null;
	}

	public boolean isDeleted() {
		return deleteAt != null;
	}
}