(ns todo-clj.core
  (:require [ring.adapter.jetty :as server]
            [compojure.core :refer [defroutes context GET]]
            [compojure.route :as route]
            [ring.util.response :as res]))

(defn ok [body]
  {:status 200
   :body body})

(defn html [res]
  (assoc res :headers {"Content-Type" "text/html; charset=utf-8" }))

(defn home-view [req]
  "<h1>ホーム画面</h1>
<a href=\"/todo\">TODO一覧</a>")

(defn home [req]
  (-> (home-view req)
      ok
      html))

(def todo-list
  [{:title "ゴミを出す"}
   {:title "朝ごはんを作る"}])

(defn todo-view [req]
  `("<h1>TODO一覧</h1>"
    "<ul>"
    ~@(for [{:keys [title]} todo-list]
        (str "<li>" title "</li>"))
    "</ul>"))

(defn todo [req]
  (-> (todo-view req)
      ok
      html))

(defroutes handler
  (GET "/" req home)
  (GET "/todo" req todo)
  (res/not-found "<h1>404 not found</h1>"))

(defonce server (atom nil))

(defn start-server []
  (when-not @server
    (reset! server (server/run-jetty #'handler {:port 3000 :join? false}))))

(defn stop-server []
  (when @server
    (.stop @server)
    (reset! server nil)))

(defn restart-server []
  (when @server
    (stop-server)
    (start-server)))



