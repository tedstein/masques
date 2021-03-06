(ns masques.model.test.group
  (:require [test.init :as test-init])
  (:require [fixtures.identity :as identity-fixture]
            [fixtures.permission :as permission-fixture]
            [masques.model.identity :as identity-model]
            [masques.test.util :as test-util])
  (:use clojure.test
        masques.model.group))

(def test-group-name "group1")

(def test-group { :name test-group-name :identity_id 1 :user_generated 1 })

(def test-permission (first permission-fixture/records))

(test-util/use-combined-login-fixture identity-fixture/fixture-map permission-fixture/fixture-map)

(deftest test-find-group
  (let [group-id (insert test-group)
        inserted-group (assoc test-group :id group-id)]
    (is group-id)
    (try
      (is (= inserted-group (find-group test-group-name)))
      (is (= inserted-group (find-group inserted-group)))
      (is (= inserted-group (find-group test-group)))
      (is (= inserted-group (find-group group-id)))
      (try
        (find-group 1.0)
        (is false "Expected an exception for an invalid group.")
        (catch Throwable t)) ; Do nothing. this is the expected result.
      (finally
        (destroy-record { :id group-id })))))

(deftest test-group-id
  (let [group-record-id (insert test-group)
        inserted-group (assoc test-group :id group-record-id)]
    (is group-record-id)
    (try
      (is (= group-record-id (group-id test-group-name)))
      (is (= group-record-id (group-id inserted-group)))
      (is (= group-record-id (group-id test-group)))
      (is (= group-record-id (group-id group-record-id)))
      (try
        (group-id 1.0)
        (is false "Expected an exception for an invalid group.")
        (catch Throwable t)) ; Do nothing. this is the expected result.
      (finally
        (destroy-record { :id group-record-id })))))

(deftest test-find-groups
  (let [group-id (insert test-group)
        inserted-group (assoc test-group :id group-id)]
    (is group-id)
    (try
      (is (= (find-groups [group-id]) [inserted-group])) 
      (is (= (find-groups [-1]) []))
      (finally
        (destroy-record { :id group-id })))))

(deftest test-read-permissions 
  (let [group-id (insert test-group)
        inserted-group (assoc test-group :id group-id)]
    (is group-id)
    (try
      (let [group-permission-id (add-read-permission test-group test-permission)]
        (is group-permission-id)
        (is (has-read-permission? test-group test-permission))
        (is (any-group-has-read-permission? [test-group] test-permission))
        (is (not (has-write-permission? test-group test-permission)))
        (is (not (any-group-has-write-permission? [test-group] test-permission)))
        (remove-read-permission test-group test-permission)
        (is (not (has-read-permission? test-group test-permission)))
        (is (not (any-group-has-read-permission? [test-group] test-permission)))
        (is (not (has-write-permission? test-group test-permission)))
        (is (not (any-group-has-write-permission? [test-group] test-permission))))
      (finally
        (destroy-record { :id group-id })))))

(deftest test-read-permissions 
  (let [group-id (insert test-group)
        inserted-group (assoc test-group :id group-id)]
    (is group-id)
    (try
      (let [group-permission-id (add-write-permission test-group test-permission)]
        (is group-permission-id)
        (is (not (has-read-permission? test-group test-permission)))
        (is (not (any-group-has-read-permission? [test-group] test-permission)))
        (is (has-write-permission? test-group test-permission))
        (is (any-group-has-write-permission? [test-group] test-permission))
        (remove-write-permission test-group test-permission)
        (is (not (has-read-permission? test-group test-permission)))
        (is (not (any-group-has-read-permission? [test-group] test-permission)))
        (is (not (has-write-permission? test-group test-permission)))
        (is (not (any-group-has-write-permission? [test-group] test-permission))))
      (finally
        (destroy-record { :id group-id })))))

(deftest test-default-groups
  (add-group-delete-listener removed-deleted-group-permissions)
  (let [test-identity (identity-model/current-user-identity)]
    (add-default-groups test-identity)
    (let [added-groups (find-identity-groups test-identity)]
      (is (= (count default-groups) (count added-groups)))
      (doseq [group added-groups]
        (is (contains? default-groups (:name group)))
        (doseq [read-permission (:read (get default-groups (:name group)))]
          (has-read-permission? group read-permission))
        (doseq [write-permission (:write (get default-groups (:name group)))]
          (has-write-permission? group write-permission))
        (doseq [none-permission (:none (get default-groups (:name group)))]
          (has-none-permission? group none-permission))))
    (remove-deleted-identity-groups test-identity)
    (is (empty? (find-identity-groups test-identity))))
  (remove-group-delete-listener removed-deleted-group-permissions))