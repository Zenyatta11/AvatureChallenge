# Avature Challenge - Jobberwocky Job Board
====================

## Service Execution
--------------------------------

### Step 1 (Optional):
Run the `jobberwocky-extra-source` service on port 8081 (Or modify it accordingly in the `application.yml`

### Step 2:
Open DockerDesktop and wait for the engine to be running.

### Step 3:
In one terminal, run `docker-compose up` to start the containers, then, in a separate terminal, run the service with:
- Linux: `./gradlew bootRun`
- Windows: `gradlew bootRun`

You should be able to access the Swagger at `http://localhost:8080/swagger-ui/index.html#/` and the healthcheck endpoint at `http://localhost:8080/health`

## Postman
A collection of three endpoints are provided. 

### Get Jobs

This endpoint will return a page of jobs and has the following optional parameters:
- page: The page number offset (Default 0)
- pageSize: The size of each page (maximum is 100, default 20)
- name: The title to search for
- salaryMin: The minimum salary to search for (inclusive)
- salaryMax: The maximum salary to search for (exclusive)
- country: The country to search for
- skills: An array of skills to search for

It will return a list of jobs that match the criteria (if it were to exist). It will prioritize internal jobs and then return external jobs from the jobberwocky-extra-service API. Should a page be partially filled with internal jobs, it will be completed with external jobs.

### New Job

This endpoint will create a new job posting. The following parameters are mandatory:
- title: Name of the posting
- salary: Integer of the monthy income
- location: String of a location

The skills body parameter is optional. It is an array of strings.

### Subscription

This endpoint will subscribe a user to alert via email if a job is posted that matches the criteria. There is no endpoint to delete an alert, however, a workaround would be putting nonsense in the parameters.

The email is a mandatory paramteter, the rest of the criterias are optional.
- email
- title
- minSalary
- maxSalary
- skills
- location

**Because of how the system works internally, Swagger shows that the location and skills are objects of strings, however that is not the case. Location is a string and Skills is an array of strings.**

Due to time limitations, unit tests are not provided, however, Postman calls work out-of-the-box.
Any parameters to customize (specifically e-mail related) can be found in `application.yml`