local typedefs=require "kong.db.schema.typedefs"

return{
  name="custom-auth",
  feild{
    {consumer=typedefs.no_consumer},
    {protocols=typedefs.protocols_http},
    {config={
      type="record",
      feild={
        {auth_service_url={
          type="string",
          required=true,
          default="http://localhost:9898/auth/v1/ping",
          description="The URL of the authentication service."
        }}
      }
    }}
  }
}