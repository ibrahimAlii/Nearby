package ib.project.nearby.data.model

data class Response(
	val suggestedFilters: SuggestedFilters? = null,
	val totalResults: Int? = null,
	val suggestedRadius: Int? = null,
	val query: String? = null,
	val headerFullLocation: String? = null,
	val warning: Warning? = null,
	val headerLocationGranularity: String? = null,
	val groups: List<GroupsItem?>? = null,
	val suggestedBounds: SuggestedBounds? = null,
	val headerLocation: String? = null
)
