# Intuit Demo Serverless User Service
An example of the intuit demo user service wtih Cognito and Lambdas.

## Structure
serverless.yml in the root directory describes the services and how they should be deployed.
Tests are in the test directory.

## Setup

### Install Prerequisites
Install python >= 3.6.7

```bash
# Install serverless (the deploy tool)
npm install -g serverless
```
### Setup Project
```bash
# Install the project's node dependencies
npm install

# Setup pipenv
pip install pipenv
pipenv install -d

# Run tests
pipenv run python setup.py test
```
