# MAGIC.md — ae3.pkg.net.ndm.client

## For keeper-ae3

`project.inf` titles this "NDM NDSS Client". It requires the packages `ae3.api`, `ae3.sdk`,
`ae3.network` and `ae3.web`, while `.classpath` names only the `/ae3.api` and `/ae3.sdk` projects.
Read it as the client half of the NDMS/NDSS service: 41 CommonJS modules under
`ae3-packages/ndm.client/resources/lib/com.ndmsystems.ndm.client/`, and four Java classes totalling
257 lines under `/Volumes/workspace/myx/ae3.pkg.net.ndm.client/java/com/ndmsystems/ndmc/`.

## What it registers in a running AE3 service

`/Volumes/workspace/myx/ae3.pkg.net.ndm.client/ae3-packages/ndm.client/settings/` binds it in five
places:

- `service/ndm.client.json` — `NdmCloudService` as an `ae3.Service`, `augments` `["server","client"]`,
  requiring `network` and `periodic`.
- `periodic/hourly/ndm.client-register.json` — `PeriodicRegistration` as an hourly executable.
- `shell/ndm.client.json` — `Command` as the shell entry, requiring `request-target` and
  `request-source`.
- `web/hosts/ndmc.local.json` and `web/hosts/ndmp.local.json` — two `ae3.web/Share` hosts, named
  `ndmc.local` and `ndmp.local`.
- `settings/ndm.ndss-client/readme.txt` is the per-instance recipe: a `<client_instance_name>.json` of
  type `ndm.client/Connection`, carrying the service `host`/`key`/`pass`, an `override` block of
  handshake and TLS flags, and a `uhp` block.

## The Java half is thin, and two of the four classes are inert

- `NdmLicenseStatic`, 124 lines, is the real one: the WCN check digit (`exportCalcWCN8th`), a random
  WCN, and three license label formatters. `NdmUtilsStatic`, 80 lines, has one real method, reading a
  fixed-width text column into map keys.
- `NdmLicenseObject` is 8 lines with an empty body. `NdmpLink`'s two static factories `fromIndexKey`
  and `fromLocalKey` both `return null`, as do `getDeviceEcPublicBase64` and
  `getServiceEcPublicBase64`, so only its constructor's `serviceId` and `toString` do anything.
- `.../java/com/ndmsystems/ndcl/` holds no source at all: a `readme.txt` reading `doesn't work
  (compile and deploy) yet 8(`, and a `.gitignore` naming two class files (`FirmwareVersionComparator`,
  `SoftwareVersionComparator`) that no source in either checkout produces.

## Gotcha: the build stamp this package sends is the engine's, and it can be the bare number

`Client.js:97` and `:98` put `___ECMA_IMPL_VERSION_STRING___` on the wire as `version` and inside
`fw`. That identifier is not this package's own: `ae3.sys`'s
`ru/myx/ae3/ecma/global/ImplBaseGlobalGlobal.java:74` defines it from `Engine.VERSION_STRING` in
every ECMA global. So whatever the engine's stamp is, is what an NDSS server is told.

On `/Volumes/workspace/myx/acm-cvs/sys-current` that stamp is the bare `679`, because the axiom's
`version/version` is a self-symlink rather than a file. `ae3.sys/MAGIC.md` carries the measurement and
the zero-link control that reads `5.0.679/C081`. Nothing here can repair it, and nothing here parses
the field back. Whether NDSS reads `fw` is outside this tree and was not checked.

## Gotcha: both web hosts name modules that are not in the tree

`web/hosts/ndmp.local.json` resolves to `com.ndmsystems.ndm.client/web/NdmpWebShare`, and
`NdmcWebShare`, the one module `web/` does hold, requires `./Index` from its `index` getter.

Neither exists. `NdmpWebShare` occurs in 0 files in any copy, and git history of the project holds no
commit for it. `Index*` occurs in 0 files in the project's `web/` folder in every copy measured — this
tree, the `ws-myx.ae3-devel` checkout, `acm-cvs/sys-current` and `acm-cvs/sys-release` — and git
history holds none either. The controls, run in the same calls, find `NdmcWebShare` 1 file per copy and
its history in three commits.

The reference is resolved lazily, which is why neither fault shows at start-up:
`/Volumes/workspace/myx/ae3.sys.pkg.i3.web/ae3-packages/ae3.web.server/resources/lib/ru.myx.ae3.internal/web/HostRouter.js:193`
runs `require(reference.reference)` inside `getHandlerForHost`, not while the descriptors load. So a
request for host `ndmp.local` throws at that line, and the first request that reads `NdmcWebShare`'s
`index` throws one level deeper. Both are source readings, not runs.

Both trees carry identical bytes: `ndmp.local.json` md5 `25271836bc289ef264f7631e1fe0d4d4` and
`NdmcWebShare.js` md5 `2a0c6649a9e19f0d34c70c0c902c8378`, in the `eclipse-ae3` project tree and in
`/Users/myx/Workspaces/ws-myx.ae3-devel/source/ae3/ae3.pkg.net.ndm.client`.

## Gotcha: this share's authenticationProvider is null, so the exception gate never passes

`NdmcWebShare.js:14-16` sets `authenticationProvider : { value : null }`. `ae3.web/Share` leaves the
same property `undefined` at `ae3.web/Share.js:43-45`, so the null is deliberate here and not an
inherited default.

`Share.js:213` reads it with optional chaining:
`isAdmin = !!(context.client && share.authenticationProvider?.authCheckMembership(context.client, 'admin'))`.
With a null provider that expression is always false, so every client gets
`makeServerFailureLayout(String(e.messageContent ?? "Server error"))` and the full layout never leaves
the process. Any diagnostic that expects a server-side dump from a host on this share is reading an
older build, a different share, or a console log.

The two hosts on this package are `ndmc.local` and `ndmp.local`. Both name modules the tree does not
hold, so neither reaches this line today. The byte-identical copies are the `eclipse-ae3` project tree
and `/Users/myx/Workspaces/ws-myx.ae3-devel/source/ae3/ae3.pkg.net.ndm.client`.
