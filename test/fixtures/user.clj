(ns fixtures.user
  (:require [fixtures.identity :as identity-fixture]))

(def fixture-table-name :users)

(def records [
  { :id 1
    :name "test-user"
    :encrypted_password "jgMGBQa8DwKN1UzMlg2iTOPW7TI6xx5CzOWjWczChYg=" ; the test password is "password"
    :salt "804003354"
    :encrypted_password_algorithm "SHA-256"
    :encrypted_password_n 1000
    :public_key ""
    :public_key_algorithm "RSA" 
    :private_key ""
    :private_key_algorithm "RSA"
    :private_key_encryption_algorithm "DES" }
  { :id 2
    :name "test-identity"
    :encrypted_password "jgMGBQa8DwKN1UzMlg2iTOPW7TI6xx5CzOWjWczChYg=" ; the test password is "password"
    :salt "804003354"
    :encrypted_password_algorithm "SHA-256"
    :encrypted_password_n 1000
    :public_key "blah"
    :public_key_algorithm "RSA"
    :private_key ""
    :private_key_algorithm "RSA"
    :private_key_encryption_algorithm "DES" }])

(def fixture-map { :table fixture-table-name :records records :required-fixtures [identity-fixture/fixture-map] })