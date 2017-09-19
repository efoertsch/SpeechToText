package com.fisincorporated.speechtotext.googlespeech.objectaccess;

import com.fisincorporated.speechtotext.googlespeech.BaseJson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


/**
 * GCS Object Access info/authority
 */
public class ObjectAccess extends BaseJson {

    @SerializedName("etag")
    @Expose
    private String etag;

    @SerializedName("bucket")
    @Expose
    private String bucket;

    @SerializedName("entity")
    @Expose
    private String entity;

    @SerializedName("entityId")
    @Expose
    private String entityId;

    @SerializedName("selfLink")
    @Expose
    private String selfLink;

    @SerializedName("object")
    @Expose
    private String object;

    @SerializedName("kind")
    @Expose
    private String kind;

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("projectTeam")
    @Expose
    private ProjectTeam projectTeam;

    @SerializedName("email")
    @Expose
    private String email;

    @SerializedName("domain")
    @Expose
    private String domain;

    @SerializedName("role")
    @Expose
    private String role;

    @SerializedName("generation")
    @Expose
    private String generation;

    public String getEtag() {
        return etag;
    }

    public void setEtag(String etag) {
        this.etag = etag;
    }

    public String getBucket() {
        return bucket;
    }

    public void setBucket(String bucket) {
        this.bucket = bucket;
    }

    public String getEntity() {
        return entity;
    }

    public void setEntity(String entity) {
        this.entity = entity;
    }

    public String getEntityId() {
        return entityId;
    }

    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }

    public String getSelfLink() {
        return selfLink;
    }

    public void setSelfLink(String selfLink) {
        this.selfLink = selfLink;
    }

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ProjectTeam getProjectTeam() {
        return projectTeam;
    }

    public void setProjectTeam(ProjectTeam projectTeam) {
        this.projectTeam = projectTeam;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getGeneration() {
        return generation;
    }

    public void setGeneration(String generation) {
        this.generation = generation;
    }
}

