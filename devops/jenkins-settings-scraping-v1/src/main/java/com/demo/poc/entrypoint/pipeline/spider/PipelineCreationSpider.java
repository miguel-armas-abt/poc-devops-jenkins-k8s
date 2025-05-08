package com.demo.poc.entrypoint.pipeline.spider;

import com.demo.poc.commons.helper.DriverHelper;
import com.demo.poc.commons.helper.SleepHelper;
import com.demo.poc.commons.logging.Logger;
import com.demo.poc.commons.properties.PropertiesReader;
import com.demo.poc.commons.properties.configuration.Pipeline;
import com.google.inject.Inject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class PipelineCreationSpider {

  private final PropertiesReader propertiesReader;
  private final DriverHelper driverHelper;
  private final SleepHelper sleepHelper;

  public void configureNewPipeline(ChromeDriver driver) {
    Logger.startLog.accept(this.getClass().getSimpleName());
    selectNewPipeline(driver);

    WebDriverWait wait = driverHelper.getWebDriverWait(driver);
    Pipeline pipeline = propertiesReader.get().getNewPipeline();

    setGitHubProject(driver, pipeline);
    selectGitSCM(driver);
    setRepository(driver, pipeline);
    setJenkinsFile(driver, pipeline);

    WebElement saveButton = wait.until(ExpectedConditions.elementToBeClickable(
        By.xpath("//div[@id='bottom-sticker']//button[@name='Submit' and contains(@class, 'jenkins-submit-button')]")));
    saveButton.click();
    Logger.endLog.accept(this.getClass().getSimpleName());
  }

  private void selectNewPipeline(ChromeDriver driver) {
    goToNewPipeline(driver);

    WebDriverWait wait = driverHelper.getWebDriverWait(driver);
    String pipelineName = propertiesReader.get().getNewPipeline().getName();

    WebElement nameInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("name")));
    nameInput.sendKeys(pipelineName);

    WebElement pipelineOption = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("li.org_jenkinsci_plugins_workflow_job_WorkflowJob")));
    pipelineOption.click();

    WebElement okButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("ok-button")));
    okButton.click();
  }

  private void goToNewPipeline(ChromeDriver driver) {
    sleepHelper.basicSleep();

    WebDriverWait wait = driverHelper.getWebDriverWait(driver);

    WebElement homeLink = wait.until(ExpectedConditions.elementToBeClickable(By.id("jenkins-home-link")));
    homeLink.click();

    WebElement newItemSpan = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("span.task-link-wrapper")));
    WebElement newItemLink = newItemSpan.findElement(By.xpath(".//a[contains(@href, '/view/all/newJob')]"));
    newItemLink.click();
  }

  private void setJenkinsFile(ChromeDriver driver, Pipeline pipeline) {
    WebDriverWait wait = driverHelper.getWebDriverWait(driver);

    WebElement scriptPathSection = wait.until(ExpectedConditions.visibilityOfElementLocated(
        By.xpath("//div[contains(@class, 'jenkins-form-item') and .//div[text()='Script Path']]")));
    driver.executeScript("arguments[0].scrollIntoView(true);", scriptPathSection);

    WebElement scriptPathInput = wait.until(ExpectedConditions.presenceOfElementLocated(
        By.xpath("//input[@name='_.scriptPath' and @type='text' and contains(@class, 'jenkins-input')]")));
    scriptPathInput.clear();
    scriptPathInput.sendKeys(pipeline.getJenkinsFilePath());
  }

  private void setGitHubProject(ChromeDriver driver, Pipeline pipeline) {
    WebDriverWait wait = driverHelper.getWebDriverWait(driver);
    WebElement githubProjectCheckboxLabel = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//label[text()='GitHub project']")));
    githubProjectCheckboxLabel.click();

    WebElement projectUrlInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("_.projectUrlStr")));
    projectUrlInput.sendKeys(pipeline.getGithubProject());
  }

  private void selectGitSCM(ChromeDriver driver) {
    WebDriverWait wait = driverHelper.getWebDriverWait(driver);

    WebElement pipelineSection = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("pipeline")));
    driver.executeScript("arguments[0].scrollIntoView(true);", pipelineSection);

    WebElement definitionDropdown = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//select[contains(@class, 'jenkins-select__input') and contains(@class, 'dropdownList')]")));
    driver.executeScript("arguments[0].click();", definitionDropdown);

    WebElement optionPipelineScript = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//select[contains(@class, 'jenkins-select__input') and contains(@class, 'dropdownList')]/option[contains(text(), 'Pipeline script from SCM')]")));
    optionPipelineScript.click();

    WebElement scmDropdown = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//select[contains(@class, 'jenkins-select__input') and contains(@class, 'dropdownList')]")));
    driver.executeScript("arguments[0].click();", scmDropdown);

    WebElement optionGit = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//select[contains(@class, 'jenkins-select__input') and contains(@class, 'dropdownList')]/option[contains(text(), 'Git')]")));
    optionGit.click();
  }

  private void setRepository(ChromeDriver driver, Pipeline pipeline) {
    WebDriverWait wait = driverHelper.getWebDriverWait(driver);

    WebElement repositoryUrlInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("_.url")));
    repositoryUrlInput.sendKeys(pipeline.getRepositoryUrl());

    WebElement branchesToBuildSection = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(@class, 'jenkins-form-item') and .//div[text()='Branches to build']]")));
    driver.executeScript("arguments[0].scrollIntoView(true);", branchesToBuildSection);

    WebElement branchSpecifierInput = wait.until(ExpectedConditions.presenceOfElementLocated(
        By.xpath("//div[@name='branches']//input[@name='_.name' and contains(@class, 'jenkins-input')]")));

    branchSpecifierInput.clear();
    branchSpecifierInput.sendKeys(pipeline.getBranch());
  }
}
