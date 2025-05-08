package com.demo.poc.commons.properties.configuration;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Pipeline {

  private String name;
  private String githubProject;
  private String repositoryUrl;
  private String branch;
  private String jenkinsFilePath;
}
