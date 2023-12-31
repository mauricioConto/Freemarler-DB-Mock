package com.condor.audit.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Setter
@Getter
public class Person {

        private String institutionId;
        private String personNameTokenId;
        private String personAddressTokenId;
        private String ssnTokenId;
        private String programManagerId;
        private String externalPersonId;
        private Boolean active;
        private String createdAt;

        public String getInstitutionId() {
                return institutionId;
        }

        public void setInstitutionId(String institutionId) {
                this.institutionId = institutionId;
        }

        public String getPersonNameTokenId() {
                return personNameTokenId;
        }

        public void setPersonNameTokenId(String personNameTokenId) {
                this.personNameTokenId = personNameTokenId;
        }

        public String getPersonAddressTokenId() {
                return personAddressTokenId;
        }

        public void setPersonAddressTokenId(String personAddressTokenId) {
                this.personAddressTokenId = personAddressTokenId;
        }

        public String getSsnTokenId() {
                return ssnTokenId;
        }

        public void setSsnTokenId(String ssnTokenId) {
                this.ssnTokenId = ssnTokenId;
        }

        public String getProgramManagerId() {
                return programManagerId;
        }

        public void setProgramManagerId(String programManagerId) {
                this.programManagerId = programManagerId;
        }

        public String getExternalPersonId() {
                return externalPersonId;
        }

        public void setExternalPersonId(String externalPersonId) {
                this.externalPersonId = externalPersonId;
        }

        public Boolean getActive() {
                return active;
        }

        public void setActive(Boolean active) {
                this.active = active;
        }

        public String getCreatedAt() {
                return createdAt;
        }

        public void setCreatedAt(String createdAt) {
                this.createdAt = createdAt;
        }

        public Person() {
        }

        public Person(String institutionId, String personNameTokenId, String personAddressTokenId, String ssnTokenId, String programManagerId, String externalPersonId, Boolean active, String createdAt) {
                this.institutionId = institutionId;
                this.personNameTokenId = personNameTokenId;
                this.personAddressTokenId = personAddressTokenId;
                this.ssnTokenId = ssnTokenId;
                this.programManagerId = programManagerId;
                this.externalPersonId = externalPersonId;
                this.active = active;
                this.createdAt = createdAt;
        }

}

