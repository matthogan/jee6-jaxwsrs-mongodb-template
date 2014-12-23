# jee6-jaxwsrs-mongodb-template/

Template

## API

CRUD

Depends on an API data model.

## Data

Depends on a domain data model.

## Controllers

> EntityJaxWsRsController (com.softhog.template.api.rs)

Simple CRUD for a DTO.
Translates to/from API and Domain models.
Delegates to a service layer.

## Service Layer

> DataService (com.softhog.template.service) 

Usual abstraction to decouple business logic from API.
Delegates to data layer.

Hello (com.softhog.template.api.rs)

## Data

> SimpleMongoDao (com.softhog.template.dao)

Simple CRUD for a DTO.


