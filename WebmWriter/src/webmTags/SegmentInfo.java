package webmTags;

import webm2flv.matroska.VINT;
import webmTags.UnsignedIntegerTag;
import webmTags.DateTag;
import webmTags.CompoundTag;
import webmTags.TagFactory;
import webmTags.SegmentElement;


import java.io.IOException;
import java.util.Date;


public class SegmentInfo {

	  private static final long BLOCK_SIZE = 128;

	  private Double duration;
	  private long timecodeScale = 1000000;
	  private Date segmentDate = new Date();
	  private final long segmentPosition;

	  public SegmentInfo(final long position)
	  {
		  segmentPosition = position;
	  }
	  
	  public byte[] writeElement()
	  {
		final long bufferSize = 0; 
	    final CompoundTag segmentInfoElem = (SegmentElement) TagFactory.createTag("Segment");

	    final StringElement writingAppElem = MatroskaDocTypes.WritingApp.getInstance();
	    writingAppElem.setValue("Webm Writer");

	    final StringElement muxingAppElem = MatroskaDocTypes.MuxingApp.getInstance();
	    muxingAppElem.setValue("JEBML v1.0");

	    final DateElement dateElem = MatroskaDocTypes.DateUTC.getInstance();
	    dateElem.setDate(segmentDate);

	    // Add timecode scale
	    final UnsignedIntegerElement timecodescaleElem = MatroskaDocTypes.TimecodeScale.getInstance();
	    timecodescaleElem.setValue(timecodeScale);

	    segmentInfoElem.addChildElement(dateElem);
	    segmentInfoElem.addChildElement(timecodescaleElem);

	    if (duration != null)
	    {
	      final FloatElement durationElem = MatroskaDocTypes.Duration.getInstance();
	      durationElem.setValue(duration);
	      segmentInfoElem.addChildElement(durationElem);
	    }

	    segmentInfoElem.addChildElement(writingAppElem);
	    segmentInfoElem.addChildElement(muxingAppElem);

	    final long len = segmentInfoElem.writeElement(ioDW);
	    final VoidElement spacer = new VoidElement(BLOCK_SIZE - len);
	    spacer.writeElement(ioDW);
	    return 1;
	  }

	  public long writeElement(final DataWriter ioDW)
	  {
	    final MasterElement segmentInfoElem = MatroskaDocTypes.Info.getInstance();

	    final StringElement writingAppElem = MatroskaDocTypes.WritingApp.getInstance();
	    writingAppElem.setValue("Matroska File Writer v1.0");

	    final StringElement muxingAppElem = MatroskaDocTypes.MuxingApp.getInstance();
	    muxingAppElem.setValue("JEBML v1.0");

	    final DateElement dateElem = MatroskaDocTypes.DateUTC.getInstance();
	    dateElem.setDate(segmentDate);

	    // Add timecode scale
	    final UnsignedIntegerElement timecodescaleElem = MatroskaDocTypes.TimecodeScale.getInstance();
	    timecodescaleElem.setValue(timecodeScale);

	    segmentInfoElem.addChildElement(dateElem);
	    segmentInfoElem.addChildElement(timecodescaleElem);

	    if (duration != null)
	    {
	      final FloatElement durationElem = MatroskaDocTypes.Duration.getInstance();
	      durationElem.setValue(duration);
	      segmentInfoElem.addChildElement(durationElem);
	    }

	    segmentInfoElem.addChildElement(writingAppElem);
	    segmentInfoElem.addChildElement(muxingAppElem);

	    final long len = segmentInfoElem.writeElement(ioDW);
	    final VoidElement spacer = new VoidElement(BLOCK_SIZE - len);
	    spacer.writeElement(ioDW);
	    return 1;
	  }

	  public void update(final DataWriter ioDW)
	  {
	    LOG.debug("Updating segment info header");
	    final long startingPos = ioDW.getFilePointer();
	    ioDW.seek(myPosition);
	    writeElement(ioDW);
	    ioDW.seek(startingPos);
	  }

	  public double getDuration()
	  {
	    return duration;
	  }

	  public void setDuration(final double duration)
	  {
	    this.duration = duration;
	  }

	  public Date getDate()
	  {
	    return segmentDate;
	  }

	  public void setDate(final Date date)
	  {
	    this.segmentDate = date;
	  }

	  public long getTimecodeScale()
	  {
	    return timecodeScale;
	  }

	  public void setTimecodeScale(final long timecodeScale)
	  {
	    this.timecodeScale = timecodeScale;
	  }	
	
	
}
