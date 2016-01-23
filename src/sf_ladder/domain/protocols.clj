(ns sf-ladder.domain.protocols)

(defprotocol Component
  "A system component, used to represent any singletons in the system."
  (start [component])
  (stop [component]))
