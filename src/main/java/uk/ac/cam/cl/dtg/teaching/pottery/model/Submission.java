/*
 * pottery-backend-interface - Backend API for testing programming exercises
 * Copyright © 2015 Andrew Rice (acr31@cam.ac.uk)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package uk.ac.cam.cl.dtg.teaching.pottery.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.ImmutableSet;
import java.util.Date;

public class Submission {

  public static final String STATUS_PENDING = "PENDING";

  public static final String STATUS_STEPS_RUNNING = "STEPS_RUNNING";
  public static final String STATUS_STEPS_FAILED = "STEPS_FAILED";
  public static final String STATUS_STEPS_COMPLETE = "STEPS_COMPLETE";

  public static final String STATUS_OUTPUT_RUNNING = "OUTPUT_RUNNING";
  public static final String STATUS_OUTPUT_FAILED = "OUTPUT_FAILED";
  public static final String STATUS_OUTPUT_COMPLETE = "OUTPUT_COMPLETE";

  public static final String STATUS_COMPLETE = "COMPLETE";

  private final String repoId;
  private final String tag;
  private final String output;
  private final Date dateScheduled;
  private final long waitTimeMs;
  private final String status;
  private final boolean needsRetry;

  @JsonCreator
  public Submission(
      @JsonProperty("repoId") String repoId,
      @JsonProperty("tag") String tag,
      @JsonProperty("output") String output,
      @JsonProperty("dateScheduled") Date dateScheduled,
      @JsonProperty("waitTimeMs") long waitTimeMs,
      @JsonProperty("status") String status,
      @JsonProperty("needsRetry") boolean needsRetry) {
    super();
    this.repoId = repoId;
    this.tag = tag;
    this.output = output;
    this.dateScheduled = dateScheduled;
    this.waitTimeMs = waitTimeMs;
    this.status = status;
    this.needsRetry = needsRetry;
  }

  public static Builder builder(String repoId, String tag) {
    return new Builder(repoId, tag);
  }

  public boolean isNeedsRetry() {
    return needsRetry;
  }

  public Date getDateScheduled() {
    return dateScheduled;
  }

  public String getRepoId() {
    return repoId;
  }

  public String getTag() {
    return tag;
  }

  public String getStatus() {
    return status;
  }

  public static class Builder {

    private final Date dateScheduled;
    private final String repoId;
    private final String tag;
    private String output;
    private String status;
    private boolean needsRetry;
    private long waitTimeMs;

    private Builder(String repoId, String tag) {
      this.repoId = repoId;
      this.tag = tag;
      this.status = STATUS_PENDING;
      this.dateScheduled = new Date();
    }

    public Builder setStatus(String status) {
      this.status = status;
      return this;
    }

    public Builder setRetry() {
      this.needsRetry = true;
      return this;
    }

    public Builder setStarted() {
      this.waitTimeMs = System.currentTimeMillis() - dateScheduled.getTime();
      this.output = null;
      return this;
    }

    public Builder setOutput(String output) {
      this.output = output;
      return this;
    }

    public Submission build() {
      return new Submission(
          repoId,
          tag,
          output,
          dateScheduled,
          waitTimeMs,
          status,
          needsRetry);
    }
  }

  public boolean isComplete() {
    return ImmutableSet.of(
            STATUS_COMPLETE,
            STATUS_STEPS_FAILED,
            STATUS_OUTPUT_FAILED)
        .contains(status);
  }

  @Override
  public String toString() {
    return "Submission{"
        + "repoId='"
        + repoId
        + '\''
        + ", tag='"
        + tag
        + '\''
        + ", output='"
        + output
        + '\''
        + ", waitTimeMs="
        + waitTimeMs
        + ", dateScheduled="
        + dateScheduled
        + ", status='"
        + status
        + '\''
        + ", needsRetry="
        + needsRetry
        + '\''
        + '}';
  }
}
