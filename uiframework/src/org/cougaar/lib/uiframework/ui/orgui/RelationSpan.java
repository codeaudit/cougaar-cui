
package org.cougaar.lib.uiframework.ui.orgui;

public class RelationSpan {
  private String relative = null;
  private long startTime = 0;
  private long endTime = -1;

  public RelationSpan (String other, long start, long end) {
    relative = other;
    startTime = start;
    endTime = end;
  }

  public String getRelative () {
    return relative;
  }

  public long getStartTime () {
    return startTime;
  }

  public long getEndTime () {
    return endTime;
  }

  public boolean contains (long t) {
    return startTime <= t && t < endTime;
  }

  public boolean adjoins (RelationSpan other) {
    return other.relative.equals(relative) &&
      other.startTime <= endTime && startTime <= other.endTime;
  }

  public void subsume (RelationSpan other) {
    startTime = Math.min(startTime, other.startTime);
    endTime = Math.max(endTime, other.endTime);
  }
}