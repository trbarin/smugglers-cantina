(ns smugglers-cantina.css
  (:require [garden.def :refer [defstyles]]))

(def margins
  [[:.m-l-10 {:margin-left "10px"}]
   [:.m-l-2 {:margin-left "2px"}]
   [:.m-b-10 {:margin-bottom "10px"}]])

(def screen
  (concat
   [[:input:focus
     {:outline "1px solid red"
      :outline-radius "3px"}]
    [:select:focus
     {:outline "1px solid red"
      :outline-radius "3px"}]
    [:body {:padding "0"
            :margin "0"
            :font-family "Roboto;sans-serif"}]
    [:.header-logo {:height "60px"}]
    [:.header
     {:background-color :black
      :padding "5px"}]
    [:.page
     {:padding "0 20px"}]
    [:.page-header
     {:color :red
      :-webkit-text-stroke "1px black"
      }]
    [:.text-field
     {:outline :none
      :width "100%"
      :border-radius "3px"
      :height "35px"
      :border "1px solid #aaa"
      :font-weight :bold
      :font-size "16px"
      :padding-left "10px"}]
    [:.dropdown-field
     {:width "100%"
      :background-color :transparent
      :border "1px solid #aaa"
      :border-radius "3px"
      :height "35px"
      :font-weight :bold
      :font-size "16px"
      :padding-left "10px"}]
    [:.character-sheet
     {:display :flex
      :flex-wrap :wrap}]
    [:.character-sheet-column
     {:width "350px"
      :padding "10px"}]
    [:.character-sheet-base-attributes]
    [:.characteristics-panel
     {:margin-top "20px"
      :background-color "rgba(0,0,0,0.05)"
      :padding-top "10px"
      :border-radius "10px"}]
    [:.characteristics-panel-title
     {:font-weight :bold
      :text-align :center}]
    [:.characteristics-panel-row
     {:display :flex}]
    [:.characteristics-panel-body]
    [:.characteristic
     {:width "100px"
      :flex-grow 1
      :display :flex
      :justify-content :space-around
      :margin-bottom "10px"}]
    [:.characteristic-inner
     {:display :flex
      :flex-direction :column
      :align-items :center
      :width "80px"
      :background-color "rgba(0,0,0,0.2)"
      :border-radius "10px"}]
    [:.characteristic-value
     {:font-size "30px"
      :margin-top "15px"
      :font-weight :bold
      :background-color :white
      :width "40px"
      :text-align :center
      :border-radius "20px"}]
    [:.derived-attributes
     {:background-color "rgba(0,0,0,0.05)"
      :padding "10px"
      :border-radius "10px"}]
    [:.derived-attributes-row
     {:display :flex
      :width "100%"}]
    [:.derived-attribute
     {:flex-grow 1
      :display :flex
      :justify-content :space-around
      :padding "5px"}]
    [:.derived-attribute-inner
     {:height "80px"
      :width "140px"
      :text-align :center
      :background-color :white
      :border-radius "10px"
      :border "1px solid #aaa"
      :display :flex
      :flex-direction :column}]
    [:.derived-attribute-content
     {:flex-grow 1}]
    [:.derived-attribute-title
     {:background-color "rgba(0,0,0,0.2)"
      :border-radius "10px"
      :padding "2px"}]
    [:.wounds-panel
     {:height "100%"
      :display :flex
      :align-items :stretch}]
    [:.wounds-panel-column
     {:width "70px"
      :display :flex
      :flex-direction :column}]
    [:.wounds-panel-column-title
     {:font-size "14px"}]
    [:.wounds-panel-column-right
     {:border-left "1px solid #aaa"}]
    [:.wounds-panel-column-content
     {:flex-grow 1}]
    [:.wound-threshold
     {:font-size "30px"
      :font-weight :bold}]
    [:.skills-panel
     {:margin-top "20px"
      :background-color "rgba(0,0,0,0.05)"
      :border-radius "10px"}]
    [:.skills-panel-title
     {:font-weight :bold
      :text-align :center
      :font-size "20px"
      :border-top-left-radius "10px"
      :border-top-right-radius "10px"
      :background-color "rgba(0,0,0,0.2)"}]
    [:.skills-subtitle
     {:font-weight :bold
      :margin-top "10px"
      :background-color "rgba(0,0,0,0.2)"}]
    [:.skills-items
     {:padding "10px"}]
    [:.skill-item
     {:padding "5px"
      :border-bottom "1px solid #aaa"}]
    [:.skill-title
     {:flex-grow 1}]
    [:.skill-rank
     {:width "35px"}]
    [:.skill-rank-input
     {:width "35px"
      :height "30px"}]
    [:.skill-dice
     {:width "135px"
      :display :flex
      :justify-content :flex-end}]
    [:.ability-die-image
     {:height "20px"}]
    [:.proficiency-die-image
     {:height "15px"}]]
   margins))