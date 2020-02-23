(ns smugglers-cantina.rules.skills)

(def skills
  [{:name "Astrogation"
    :key :astrogation
    :characteristic :intellect}
   {:name "Athletics"
    :key :athletics
    :characteristic :brawn}
   {:name "Brawl"
    :key :brawl
    :characteristic :brawn}
   {:name "Charm"
    :key :charm
    :characteristic :presence}
   {:name "Coercion"
    :key :coercion
    :characteristic :willpower}
   {:name "Computers"
    :key :computers
    :characteristic :intellect}
   {:name "Cool"
    :key :cool
    :characteristic :presence}
   {:name "Coordination"
    :key :coordination
    :characteristic :agility}
   {:name "Core Worlds"
    :key :core-worlds
    :characteristic :intellect
    :type :knowledge}
   {:name "Deception"
    :key :deception
    :characteristic :cunning}
   {:name "Discipline"
    :key :discipline
    :characteristic :willpower}
   {:name "Education"
    :key :education
    :type :knowledge
    :characteristic :intellect}
   {:name "Gunnery"
    :key :gunnery
    :type :combat
    :characteristic :agility}
   {:name "Leadership"
    :key :leadership
    :characteristic :presence}
   {:name "Lore"
    :key :lore
    :type :knowledge
    :characteristic :intellect}
   {:name "Mechanics"
    :key :mechanics
    :characteristic :intellect}
   {:name "Medicine"
    :key :medicine
    :characteristic :intellect}
   {:name "Melee"
    :key :melee
    :type :combat
    :characteristic :brawn}
   {:name "Negotiation"
    :key :negotiation
    :characteristic :presence}
   {:name "Outer Rim"
    :key :outer-rim
    :type :knowledge
    :characteristic :intellect}
   {:name "Perception"
    :key :perception
    :characteristic :cunning}
   {:name "Piloting - Planetary"
    :key :piloting-planetary
    :characteristic :agility}
   {:name "Piloting - Space"
    :key :piloting-space
    :characteristic :agility}
   {:name "Ranged - Heavy"
    :key :ranged-heavy
    :type :combat
    :characteristic :agility}
   {:name "Ranged - Light"
    :key :ranged-light
    :type :combat
    :characteristic :agility}
   {:name "Resilience"
    :key :resilience
    :characteristic :brawn}
   {:name "Skulduggery"
    :key :skulduggery
    :characteristic :cunning}
   {:name "Stealth"
    :key :stealth
    :characteristic :agility}
   {:name "Streetwise"
    :key :streetwise
    :characteristic :cunning}
   {:name "Survival"
    :key :survival
    :characteristic :cunning}
   {:name "Underworld"
    :key :underworld
    :type :knowledge
    :characteristic :intellect}
   {:name "Vigilance"
    :key :vigilance
    :characteristic :willpower}
   {:name "Xenology"
    :key :xenology
    :type :knowledge
    :characteristic :intellect}])
