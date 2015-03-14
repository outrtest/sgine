# Introduction #

This page contains an outline of the first complete iteration of the UI framework.

# Details #

Core classes:
  * Document - object representation of stylized text and graphics
  * Skin - a base-class to be referenced by all skinnable components

Components:
  * Label - a simple single-line text representation
  * Text - a document-based multi-line supported stylized document representation
  * TextInput - a skinnable single-line text representation
  * TextEditor - a skinnable, document-based, multi-line supported stylized text editor
  * ListBox - a single-column multi-line view of data
  * ComboBox - a single-column multi-line view of data represented via a single entry with pop-up for selection
  * Slider - a visual slider either horizontal or vertical to represent a range and a point upon it
  * CheckBox - a toggleable visual element able to be associated with a group
  * ComboBox - a selectable visual element able to be associated with a group
  * Tree - a hierarchical representation of data
  * Table - a mult-column and multi-line view of data
  * Window - a floating visual container with modal support
    * Frame - a window with controls
    * Popup - a window shown/hidden based on events