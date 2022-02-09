package com.example.offerdaysongs.dto;

import com.example.offerdaysongs.model.Singer;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import java.time.ZonedDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class RecordingDto {
    long id;
    String title;
    String version;
    ZonedDateTime releaseTime;
    SingerDto singer;
}
