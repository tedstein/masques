(ns fixtures.identity
  (:require [fixtures.peer :as peer-fixture]))

(def fixture-table-name :identities)

(def records [
  { :id 1
    :name "test-user"
    :public_key ""
    :public_key_algorithm "RSA"
    :peer_id 1
    :is_online nil }
  { :id 2
    :name "test-identity"
    :public_key "blah"
    :public_key_algorithm "RSA"
    :peer_id 1
    :is_online nil }
  { :id 3
    :name "test-identity2"
    :public_key "blah2"
    :public_key_algorithm "RSA"
    :peer_id 1
    :is_online nil }
  { :id 4
    :name "test-identity3"
    :public_key "blah3"
    :public_key_algorithm "RSA"
    :peer_id 1
    :is_online nil }
  { :id 5
    :name "test-identity4"
    :public_key "blah3"
    :public_key_algorithm "RSA"
    :peer_id 1
    :is_online nil }])

(def fixture-map { :table fixture-table-name :records records :required-fixtures [peer-fixture/fixture-map] })